import { Component, OnInit } from '@angular/core';
import { DropoffService } from '../dropoff.service';
import { DropOffLocation } from '../dropOffLocation';

import { UserService } from '../user.service';

@Component({
  selector: 'app-dropoff',
  templateUrl: './dropoff.component.html',
  styleUrl: './dropoff.component.css'
})
export class DropoffComponent implements OnInit{
    schedule: DropOffLocation[] = [];
    username: string | undefined;
    town: DropOffLocation[] = [];
    vol = false;
    isadmin = false;
    Display = true;
    display = true;
    location: DropOffLocation | undefined;
    Date: number[] = [2024,1, 1];
    

    constructor(
      private dropoffService: DropoffService,
      private userService: UserService
    ){
      let username = localStorage.getItem('username');
      if(username) { this.username = username; }
    }

    logout() {
      this.userService.logout();
    }

    ngOnInit(): void {
        this.getSchedule();
        this.isAdmin();
        this.display = true;
    }
    getSchedule(){
      this.dropoffService.getSchedule().subscribe(s => this.schedule = s);
    }

    volunteer(id: any, user: any){
      this.dropoffService.volunteer(id, user).subscribe();
      window.location.reload();
    }

    unvolunteer(id: any){
      this.dropoffService.unvolunteer(id).subscribe();
      window.location.reload();
    }

   addDropoffLocation(town: string, hour: number, minute: number, year: number, month: number, day: number){
      this.dropoffService.addDropoffLocation({town, date: [year, month, day], hour, minute } as DropOffLocation)
      .subscribe(newdropoff => this.schedule.push(newdropoff));
      window.location.reload();
      
   }

    isAdmin(){
      this.isadmin = this.userService.isAdmin(this.username);
    }

    toggleupdate(){
      this.Display = !this.Display;
    }

  
    updateDropoffLocation(dropoff: DropOffLocation, date: number[]){
      dropoff.date = this.Date;
      this.dropoffService.updateDropoffLocation(dropoff).subscribe();
      window.location.reload();
    }
    
    deleteLocation(id: number){
      this.dropoffService.deleteDropoffLocation(id).subscribe();
      window.location.reload();
    }
    
    getScheduleTown(town: string){
      this.dropoffService.getScheduleTown(town).subscribe(t => this.town = t);
      this.display = !this.display;
    
    }

    
}
