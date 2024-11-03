import { Component, OnInit } from '@angular/core';
import { Needs } from '../needs';
import { ActivatedRoute } from '@angular/router';
import { NeedsService } from '../needs.service';
import { Location } from '@angular/common';
import { UserService } from '../user.service';
import { CartService } from '../cart.service';

@Component({
  selector: 'app-admin-needs-detail',
  templateUrl: './admin-needs-detail.component.html',
  styleUrl: './admin-needs-detail.component.css'
})
export class AdminNeedsDetailComponent implements OnInit {
  need: Needs | undefined;
  username: string | undefined;
  id: number = this.cartService.getCartid();
  constructor(
    private route: ActivatedRoute,
    private needService: NeedsService,
    private location: Location,
    private userservice: UserService,
    private cartService: CartService
  ){
    let username = localStorage.getItem('username');
    if(username) { this.username = username; }
  }

  logout() {
    this.userservice.logout();
  }

  ngOnInit(): void {
    this.getNeed();
  }

  getNeed(): void{
    const id = Number(this.route.snapshot.paramMap.get('id'));
    this.needService.getNeed(id).subscribe((need) => {
      if(need) {
        this.need = need;
      }
    });
  }

  goBack(): void{
    this.location.back();
  }

  update(need: Needs) {
    if(this.need) {
      if(need.name == "") {
        need.name = this.need.name;
      }
      if(need.type == "") {
        need.type = this.need.type;
      }
      if(need.cost == "") {
        need.cost = this.need.cost;
      }
      if(need.quantity == "") {
        need.quantity = this.need.quantity;
      }
      this.needService.updateNeed(need).subscribe(() => this.goBack);
      location.reload();
    }
  }
}
