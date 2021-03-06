import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { FormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { NgxChartsModule } from '@swimlane/ngx-charts';

import { AppComponent } from './app.component';
import { MoistureComponent } from './moisture/moisture.component';
import { AppRoutingModule } from './app-routing.module';
import { DashboardComponent } from './dashboard/dashboard.component';
import { SensorComponent } from './sensor/sensor.component';
import { HeaderComponent } from './components/header/header.component';
import { FooterComponent } from './components/footer/footer.component';



@NgModule({
  declarations: [
    AppComponent,
    MoistureComponent,
    DashboardComponent,
    SensorComponent,
    HeaderComponent,
    FooterComponent
  ],
  imports: [
    BrowserModule, 
    FormsModule,
    HttpClientModule,
    NgxChartsModule,
    BrowserAnimationsModule,
    AppRoutingModule 
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
