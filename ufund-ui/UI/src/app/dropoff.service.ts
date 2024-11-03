import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { MessageService } from './message.service';
import { Observable, of } from 'rxjs';
import { DropOffLocation } from './dropOffLocation';
import { catchError, map, tap } from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class DropoffService {
  private dropoffURL = 'http://localhost:8080/schedule'

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

    getSchedule(): Observable<DropOffLocation []>{
      return this.http.get<DropOffLocation []>(this.dropoffURL).pipe(
        tap(_ => this.log(`fetched schedule`)),
        catchError(this.handleError<DropOffLocation []>(`getSchedule`))
      );
    }

    volunteer(id: any, user: any){
      const data = {id: id, user: id};
      const url = `${this.dropoffURL}/${id}/${user}`;
      return this.http.post<boolean>(url, data, this.httpOptions).pipe(
        tap(() => this.log(`volunteer`)),
        catchError(this.handleError<any>('volunteer'))
      );
    }
    
    unvolunteer(id: any){
      const url = `${this.dropoffURL}/${id}`;
      return this.http.put<boolean>(url, id, this.httpOptions).pipe(
        tap(_ => this.log(`unvolunteer`)),
        catchError(this.handleError<any>('unvolunteer'))
      );
    }

    addDropoffLocation(dropofflocation: DropOffLocation ){
        return this.http.post<boolean>(this.dropoffURL, dropofflocation, this.httpOptions).pipe(
          tap(() => this.log(`added dropofflocation`)),
          catchError(this.handleError<any>('adddropofflocation'))
        );
    }

    updateDropoffLocation(dropofflocation: DropOffLocation){
      return this.http.put<boolean>(this.dropoffURL, dropofflocation, this.httpOptions).pipe(
        tap(_ => this.log(`edit`)),
        catchError(this.handleError<any>('editDropoffLocation'))
      );
    }

    deleteDropoffLocation(id: number){
      const url = `${this.dropoffURL}/${id}`;
      return this.http.delete<boolean>(url, this.httpOptions).pipe(
        tap(_ => this.log(`deleted location`)),
        catchError(this.handleError<boolean>('deletelocation'))
      );
    }
    
    getScheduleTown(town: string): Observable<DropOffLocation[]>{
      const url = `${this.dropoffURL}/${town}`;
      return this.http.get<DropOffLocation[]>(url).pipe(
        tap(_ => this.log(`fetched town`)),
        catchError(this.handleError<DropOffLocation []>(`getScheduleTown`))
      );
    }
    
    getDate(dropoff: DropOffLocation){
      return dropoff.date;
    }
}
