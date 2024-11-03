import { Component, OnInit } from '@angular/core';
import { UserService } from '../user.service';
import { NeedsService } from '../needs.service';
import { Needs } from '../needs';
import { Observable, Subject, debounceTime, distinctUntilChanged, switchMap } from 'rxjs';

@Component({
  selector: 'app-adminview',
  templateUrl: './adminview.component.html',
  styleUrl: './adminview.component.css'
})
export class AdminviewComponent implements OnInit{
  cupboard: Needs[] = [];
  needs$!: Observable<Needs[]>;
  username: string | undefined;
  message: string = 'There are no needs that match your search';
  showMessage: boolean = false;
  colors: string[] = ['#f0e8c8','#ffcaaf'];
  colorAlternator: number = 0;
  hover: number = 0;
  Display = true;
  private searchTerms = new Subject<string>();
  inSearch: boolean = false;
  addMessage: string = 'Add Need';
  
  constructor(private needservice: NeedsService, private userservice: UserService) {
    let username = localStorage.getItem('username');
    if(username) { this.username = username; }
  }

  logout() {
    this.userservice.logout();
  }

  resetAlternator(): void {
    this.colorAlternator = 0;
  }

  getCupboard(): void {
    this.needservice.getCupboard().subscribe(cupboard => this.cupboard = cupboard);
  }

  getColor(): string {
    this.colorAlternator = (this.colorAlternator + 1) % 2;
    return this.colors[this.colorAlternator];
  }

  search(term: string): void {
    if (term == '') {
      this.inSearch = false;
      this.showMessage = false;
    }
    else {
      this.searchTerms.next(term);
      this.needs$.subscribe((needs: Needs[]) => {
        if (needs.length > 0) {this.showMessage = false;}
        else {this.showMessage = true;}
      });
      this.inSearch = true;
    }
  }

  ngOnInit(): void {
    this.inSearch = false;
    this.showMessage = false;
    this.getCupboard();
    this.needs$ = this.searchTerms.pipe(
      debounceTime(300),
      distinctUntilChanged(),
      switchMap((term: string) => this.needservice.searchCupboard(term))
    )
  }

  add(name: string, type: string, cost: any, quantity: any){
    this.needservice.createNeed({name, type, cost, quantity} as Needs).subscribe(newneeds => 
      {this.cupboard.push(newneeds)})
      this.Display = true;
      this.addMessage = 'Add Need';
  }
  
  toggleadd(): void {
    this.Display = !this.Display;
    if(!this.Display) {this.addMessage = 'Cancel';}
    else {this.addMessage = 'Add Need';}
  }

  delete(need: Needs): void {
    this.cupboard = this.cupboard.filter(n => n !== need);
    this.needservice.deleteNeed(need.id).subscribe();
  }
}
