import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

import { Observable, of } from 'rxjs';
import { catchError, tap } from 'rxjs/operators';

import { environment } from './environments/environment';

import { Sensor } from './sensor';
import { SrvUrl } from './SrvUrl'

@Injectable({
  providedIn: 'root'
})
export class SensorService {

  constructor(private http: HttpClient) { }

  getSensors(): Observable<Sensor[]> {
    const moistures =  this.http.get<Sensor[]>(`${environment.API_URL}/sensors`)
    .pipe(
      tap(_ => this.log('fetched sensors')),
      catchError(this.handleError<Sensor[]>('getSensors', []))
    );
    return moistures;
  }

  updateSensors(sensorList) {
    console.log('save me')
    const sensors = this.http.post<Sensor[]>(`${environment.API_URL}/sensors`, sensorList)
    .pipe(
      tap(_ => this.log('updated')),
      catchError(this.handleError<Sensor[]>('updateSensors', []))
    )
    .subscribe(sensors => {
      console.log("updated sensors: " + sensors)
    })

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
