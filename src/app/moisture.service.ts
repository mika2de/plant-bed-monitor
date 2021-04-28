import { Injectable } from '@angular/core';
import { Observable, of } from 'rxjs';

import { Multi } from './multi';
import { Single } from './single';
import { MOISTURES_24h as MOISTURES_LEFT } from './mock-24h-moistures-left';
import { MOISTURES_24h as MOISTURES_RIGHT } from './mock-24h-moistures-right';

@Injectable({
  providedIn: 'root'
})
export class MoistureService {

  constructor() { }

  getMoisturesRight(): Observable<Multi[]> {
    const moistures = of(MOISTURES_RIGHT);
    return moistures;
  }

  getMoisturesLeft(): Observable<Multi[]> {
    const moistures = of(MOISTURES_LEFT);
    return moistures;
  }
}