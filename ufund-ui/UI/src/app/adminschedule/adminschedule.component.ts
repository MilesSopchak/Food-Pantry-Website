import { Component, OnInit } from '@angular/core';
import { DropoffService } from '../dropoff.service';
import { DropOffLocation } from '../dropOffLocation';
import { UserService } from '../user.service';
import { Observable, Subject, debounceTime, distinctUntilChanged, switchMap } from 'rxjs';

@Component({
  selector: 'app-adminschedule',
  templateUrl: './adminschedule.component.html',
  styleUrl: './adminschedule.component.css'
})
export class AdminscheduleComponent implements OnInit {
  schedule: DropOffLocation[] = [];
  username: string | undefined;
  colors: string[] = ['#f0e8c8','#ffcaaf'];
  colorAlternator: number = 0;
  message: string = 'There are no dropoff locations that match your search';
  showMessage: boolean = false;
  schedule$!: Observable<DropOffLocation[]>;
  private searchTerms = new Subject<string>();
  inSearch: boolean = false;
  Display = true;
  id = 0;
  addMessage: string = 'Add Dropoff Location';
  constructor(private dropoffService: DropoffService, private userservice: UserService){
    let username = localStorage.getItem('username');
    if(username) { this.username = username; }
  }

  getColor(): string {
    this.colorAlternator = (this.colorAlternator + 1) % 2;
    return this.colors[this.colorAlternator];
  }

  getHour(hour: number): string {
    let time = ' am';
    if(hour > 11) {time = ' pm';}
    hour %= 12;
    if(hour == 0) {hour =12;}
    return hour + time;
  }

  getMilitaryTime(hour: string): number {
    let timeArr = hour.split(' ');
    let time = Number(timeArr[0]);
    if(time == 12) {time = 0;}
    if(timeArr[1] == 'pm') {time += 12;}
    return time;
  }

  getDate(input: string): number[] {
    let strDate = input.split(',');
    let date = [0,0,0];
    for (let i = 0; i < strDate.length; i++) {
      date[i] = Number(strDate[i]);
    }
    return date;
  }

  toNumber(input: string): number {
    return Number(input);
  }

  search(term: string): void {
    if (term == '') {
      this.inSearch = false;
      this.showMessage = false;
    }
    else {
      this.searchTerms.next(term);
      this.schedule$.subscribe((needs: DropOffLocation[]) => {
        if (needs.length > 0) {this.showMessage = false;}
        else {this.showMessage = true;}
      });
      this.inSearch = true;
    }
  }

  logout() {
    this.userservice.logout();
  }

  ngOnInit(): void {
    this.inSearch = false;
    this.showMessage = false;
    this.getSchedule();
    this.schedule$ = this.searchTerms.pipe(
      debounceTime(300),
      distinctUntilChanged(),
      switchMap((term: string) => this.dropoffService.getScheduleTown(term))
    )
  }

  getSchedule(){
    this.dropoffService.getSchedule().subscribe(s => this.schedule = s);
  }

  toggleadd(): void {
    this.Display = !this.Display;
    if(!this.Display) {this.addMessage = 'Cancel';}
    else {this.addMessage = 'Add Dropoff Location';}
  }

  addDropoffLocation(
    town: string, date: number[], hour: number, minute: number, user: string){
    this.dropoffService.addDropoffLocation({town, date, hour, minute, user} as DropOffLocation)
    .subscribe(newdropoff => this.schedule.push(newdropoff));
    this.Display = true;
    this.addMessage = 'Add Dropoff Location';
    location.reload();
  }

  update(id: number) {
    this.id = id;
  }

  cancel() {
    this.id = 0;
  }

  updateDropoffLocation(old: DropOffLocation, updated: DropOffLocation) {
    if(updated.town == "") {
      updated.town = old.town;
    }
    if(updated.date == "") {
      updated.date = old.date;
    }
    else {updated.date = this.getDate(updated.date)}
    if(updated.hour == "") {
      updated.hour = old.hour;
    }
    else {updated.hour = this.getMilitaryTime(updated.hour)}
    if(updated.minute == "") {
      updated.minute = old.minute;
    }
    else {updated.minute = this.toNumber(updated.minute)}
    if(updated.user == "") {
      updated.user = old.user;
    }
    this.dropoffService.updateDropoffLocation(updated).subscribe(() => location.reload());
  }

  delete(location: DropOffLocation): void {
    this.schedule = this.schedule.filter(l => l !== location);
    this.dropoffService.deleteDropoffLocation(location.id).subscribe();
  }
}
