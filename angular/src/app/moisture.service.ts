import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

import { Observable, of } from 'rxjs';
import { catchError, tap } from 'rxjs/operators';

import { environment } from './environments/environment';

import { Multi } from './multi';
import { SrvUrl } from './SrvUrl'

@Injectable({
  providedIn: 'root'
})
export class MoistureService {

  constructor(private http: HttpClient) { }

  getMoistures(): Observable<Multi[]> {
    const moistures =  this.http.get<Multi[]>(`${environment.API_URL}/moisture/24h`)
    .pipe(
      tap(_ => this.log('fetched current moisture')),
      catchError(this.handleError<Multi[]>('getCurrentMoisture', []))
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
