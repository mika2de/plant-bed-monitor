import { Injectable } from '@angular/core';
import { Observable, of } from 'rxjs';

import { Multi } from './multi';
import { Single } from './single';
import { CURRENT_MOISTURES } from './mock-current-moisture';

@Injectable({
  providedIn: 'root'
})
export class DashboardService {

  constructor() { }

  getCurrentMoisture(): Observable<Single[]> {
    const moistures = of(CURRENT_MOISTURES);
    return moistures;
  }

}
