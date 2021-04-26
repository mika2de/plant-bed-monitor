import { Component, OnInit } from '@angular/core';

import { Multi } from "../multi"
import { MoistureService } from '../moisture.service';

@Component({
  selector: 'app-humidity',
  templateUrl: './humidity.component.html',
  styleUrls: ['./humidity.component.css']
})
export class HumidityComponent implements OnInit {

  humiditiesRight: Multi[] = [];
  humiditiesLeft: Multi[] = [];
  view: any[] = [700, 300];

  // options
  legend: boolean = true;
  showLabels: boolean = true;
  animations: boolean = true;
  xAxis: boolean = true;
  yAxis: boolean = true;
  showYAxisLabel: boolean = true;
  showXAxisLabel: boolean = true;
  xAxisLabel: string = 'Year';
  yAxisLabel: string = 'Population';
  timeline: boolean = true;
  showRefLines: boolean = true;
  showRefLabels: boolean = true;
  referenceLines = [
    { value: 15, name: 'Max' },
    { value: -13, name: 'Min' },
    { value: 2, name: 'Ideal' }
  ];

  colorScheme = {
    domain: ['#5AA454', '#E44D25', '#CFC0BB', '#7aa3e5', '#a8385d', '#aae3f5']
  };


  constructor(private humidityService: MoistureService) {}

  ngOnInit(): void {
    this.getHumiditiesLeft();
    this.getHumiditiesRight();
  }

  getHumiditiesLeft(): void {
    this.humidityService.getMoisturesLeft().subscribe(entries => this.humiditiesLeft = entries) 
  }

  getHumiditiesRight(): void {
    this.humidityService.getMoisturesRight().subscribe(entries => this.humiditiesRight = entries) 
  }

  onSelect(data): void {
    console.log('Item clicked', JSON.parse(JSON.stringify(data)));
  }

  onActivate(data): void {
    console.log('Activate', JSON.parse(JSON.stringify(data)));
  }

  onDeactivate(data): void {
    console.log('Deactivate', JSON.parse(JSON.stringify(data)));
  }
}
