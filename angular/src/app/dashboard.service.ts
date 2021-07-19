import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

import { Observable, of } from 'rxjs';
import { catchError, tap } from 'rxjs/operators';

import { environment } from './environments/environment';

import { Single } from './single';

@Injectable({
  providedIn: 'root'
})
export class DashboardService {

  constructor(private http: HttpClient) { }

  getCurrentMoisture(): Observable<Single[]> {
    const moistures =  this.http.get<Single[]>(`${environment.API_URL}/moisture/current`)
    .pipe(
      tap(_ => this.log('fetched current moisture')),
      catchError(this.handleError<Single[]>('getCurrentMoisture', []))
    );
    return moistures;
  }
  
  private handleError<T>(operation = 'operation', result?: T) {
    return (error: any): Observable<T> => {
      console.error(error);
      this.log(`${operation} failed: ${error.message}`);
      return of(result as T);
    };
  }

  private log(message: string) {
    console.log(`DashboardService: ${message}`)
  }
}
