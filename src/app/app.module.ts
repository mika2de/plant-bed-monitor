import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { FormsModule } from '@angular/forms';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { NgxChartsModule } from '@swimlane/ngx-charts';

import { AppComponent } from './app.component';
import { MoistureComponent } from './moisture/moisture.component';
import { TemperatureComponent } from './temperature/temperature.component';
import { HumidityComponent } from './humidity/humidity.component';
import { AppRoutingModule } from './app-routing.module';



@NgModule({
  declarations: [
    AppComponent,
    MoistureComponent,
    TemperatureComponent,
    HumidityComponent
  ],
  imports: [
    BrowserModule, 
    FormsModule,
    NgxChartsModule,
    BrowserAnimationsModule,
    AppRoutingModule 
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
