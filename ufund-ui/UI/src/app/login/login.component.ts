import { Component } from '@angular/core';
import { UserService } from '../user.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export class LoginComponent {
  constructor(private userservice: UserService) {}

  login(username: string, password: string)
  {
    this.userservice.login([username, password]);
  }


}
