import { Component, OnInit } from '@angular/core';
import { CartService } from '../cart.service';
import { Needs } from '../needs';
import { UserService } from '../user.service';
import { Location } from '@angular/common';
@Component({
  selector: 'app-cart',
  templateUrl: './cart.component.html',
  styleUrl: './cart.component.css'
})
export class CartComponent {
    cart: Needs[] = [];
    username: string | undefined;
    id: number = this.cartService.getCartid();
    colors: string[] = ['#f0e8c8','#ffcaaf'];
    colorAlternator: number = 0;
    
    constructor(
      private cartService: CartService,
      private userservice: UserService
    ){
      let username = localStorage.getItem('username');
      if(username) { this.username = username; }
    }

    logout() {
      this.userservice.logout();
    }

    getColor(): string {
      this.colorAlternator = (this.colorAlternator + 1) % 2;
      return this.colors[this.colorAlternator];
    }

    getCart(){
      let storeddata: string | null = localStorage.getItem('secession');
      let data: string[] = storeddata ? JSON.parse(storeddata): null
      this.cartService.getCart(Number(data[0])).subscribe(cart => this.cart = cart);
    }

    ngOnInit(): void {
        this.getCart();
    }


    deleteNeed(id: [cart: any, need: any]){
      this.cartService.deleteNeed(id).subscribe();
      window.location.reload();
    }

    checkout(id: any){
      this.cartService.checkout(id).subscribe();
      window.location.reload();
    }

}
