import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { MessageService } from './message.service';
import { Observable, of } from 'rxjs';
import { Needs } from './needs';
import { catchError, map, tap } from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class CartService {
  private cartURL = 'http://localhost:8080/cart'
  constructor(
    private http: HttpClient,
    private messageService: MessageService,
  ) { }

  private log(message: string){
    this.messageService.add(`UserService: ${message}`)
  }

  httpOptions = {
    headers: new HttpHeaders({ 'Content-Type': 'application/json' })
  };

  private handleError<T>(operation = 'operation', result?: T) {
    return (error: any): Observable<T> => {
      console.error(error);
      this.log(`${operation} failed: ${error.message}`);
      return of(result as T);
    };
  }

  getCart(id: number): Observable<Needs[]>{
    const url = `${this.cartURL}/${id}`;
    return this.http.get<Needs[]>(url).pipe(
      tap(_ => this.log(`fetched cart for user:=${id}`)),
      catchError(this.handleError<Needs[]>(`getCart id=${id}`))
    );
  }

  addNeed(id: [cart: any, need: any, quantity: any]){
    return this.http.post<boolean>(this.cartURL, id, this.httpOptions).pipe(
      tap(() => this.log(`added need`)),
      catchError(this.handleError<any>('addNeed'))
    );
  }

  getCartid(){
    let storeddata: string | null = localStorage.getItem('secession');
    let data: string[] = storeddata ? JSON.parse(storeddata): null
    return Number(data[0]);
  }

  deleteNeed(id: [cart: any, need: any]){
    return this.http.delete<boolean>(this.cartURL, {body: id}).pipe(
      tap(_ => this.log(`delete the need from cart`)),
      catchError(this.handleError<Needs[]>(`delete need`))
    );
  }

  checkout(id: any){
    const url = `${this.cartURL}/${id}`;
    return this.http.put<boolean>(url, id, this.httpOptions).pipe(
      tap(_ => this.log(`checked out`)),
      catchError(this.handleError<any>('checkout'))
    );
  }
}
