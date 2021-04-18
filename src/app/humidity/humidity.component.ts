import { Component, OnInit } from '@angular/core';
import * as shape from 'd3-shape';

import { Humidities } from "./humidities"
import { HumidityService } from '../humidity.service';

@Component({
  selector: 'app-humidity',
  templateUrl: './humidity.component.html',
  styleUrls: ['./humidity.component.css']
})
export class HumidityComponent implements OnInit {

  humidities: Humidities[] = [];
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


  curve = shape.curveBundle.beta(0.5);
  gradient = false;
  showXAxis = true;
  showYAxis = true;
  showLegend = true;
  single = [
    {
      "name": "Germany",
      "value": 20
    },
    {
      "name": "USA",
      "value": -5
    },
    {
      "name": "France",
      "value": 12
    }
  ];

  colorScheme = {
    domain: ['#5AA454', '#E44D25', '#CFC0BB', '#7aa3e5', '#a8385d', '#aae3f5']
  };


  constructor(private humidityService: HumidityService) {}

  ngOnInit(): void {
    this.getHumidities();
  }

  getHumidities(): void {
    this.humidityService.getHumidities().subscribe(entries => this.humidities = entries) 
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
