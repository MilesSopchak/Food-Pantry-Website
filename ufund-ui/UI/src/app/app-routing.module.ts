import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { HomeComponent } from './home/home.component';
import { LoginComponent } from './login/login.component';
import { SignupComponent } from './signup/signup.component';
import { UserviewComponent } from './user_view/userview.component';
import { userGuard } from './user.guard';
import { AdminviewComponent } from './admin_view/adminview.component';
import { NeedsDetailComponent } from './needs-detail/needs-detail.component';
import { CartComponent } from './cart/cart.component';
import { DropoffComponent } from './dropoff/dropoff.component';
import { AdminscheduleComponent } from './adminschedule/adminschedule.component';
import { UserscheduleComponent } from './userschedule/userschedule.component';
import { AdminNeedsDetailComponent } from './admin-needs-detail/admin-needs-detail.component';


const routes: Routes = [
  {path: 'home', component: HomeComponent}, 
  {path: 'login', component: LoginComponent},
  {path: '', redirectTo: '/home', pathMatch: 'full'}, 
  {path: 'signup', component: SignupComponent},
  {path: 'cupboard', component: UserviewComponent, canActivate: [userGuard]},
  {path: 'cupboard/admin', component: AdminviewComponent, canActivate: [userGuard]},
  {path: 'detail/:id', component: NeedsDetailComponent, canActivate: [userGuard]},
  {path: 'detail/admin/:id', component: AdminNeedsDetailComponent, canActivate: [userGuard]},
  {path: 'cart', component: CartComponent, canActivate: [userGuard]},
  {path: 'schedule', component: UserscheduleComponent, canActivate: [userGuard]},
  {path: 'schedule/admin', component: AdminscheduleComponent, canActivate: [userGuard]}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
