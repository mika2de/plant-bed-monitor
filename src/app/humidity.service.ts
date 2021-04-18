import { Injectable } from '@angular/core';
import { Observable, of } from 'rxjs';

import { Humidities } from './humidity/humidities';
import { Humidity } from './humidity/humidity';
import { HUMIDITIES } from './mock-humidity';

@Injectable({
  providedIn: 'root'
})
export class HumidityService {

  constructor() { }

  getHumidities(): Observable<Humidities[]> {
    const humidities = of(HUMIDITIES);
    return humidities;
  }
}