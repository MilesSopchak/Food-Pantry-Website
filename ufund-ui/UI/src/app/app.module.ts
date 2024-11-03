import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { HomeComponent } from './home/home.component';
import { LoginComponent } from './login/login.component';
import { SignupComponent } from './signup/signup.component';
import { HttpClientModule } from '@angular/common/http';
import { UserviewComponent } from './user_view/userview.component';
import { AdminviewComponent } from './admin_view/adminview.component';
import { NeedsDetailComponent } from './needs-detail/needs-detail.component';
import { FormsModule } from '@angular/forms';
import { CartComponent } from './cart/cart.component';
import { DropoffComponent } from './dropoff/dropoff.component';
import { UserscheduleComponent } from './userschedule/userschedule.component';
import { AdminscheduleComponent } from './adminschedule/adminschedule.component';
import { AdminNeedsDetailComponent } from './admin-needs-detail/admin-needs-detail.component';



@NgModule({
  declarations: [
    AppComponent,
    HomeComponent,
    LoginComponent,
    SignupComponent,
    UserviewComponent,
    AdminviewComponent,
    NeedsDetailComponent,
    CartComponent,
    DropoffComponent,
    UserscheduleComponent,
    AdminscheduleComponent,
    AdminNeedsDetailComponent,
    
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    FormsModule
    
    
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
