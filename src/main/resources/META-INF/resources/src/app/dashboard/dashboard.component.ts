import { Component, OnInit } from '@angular/core';
import * as shape from 'd3-shape';

import { Single } from "./../single"
import { DashboardService } from '../dashboard.service';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent implements OnInit {

  view: any[] = [700, 400];
  showXAxis = true;
  showYAxis = true;
  gradient = false;
  showLegend = true;
  showXAxisLabel = false;
  xAxisLabel = 'hour';
  showYAxisLabel = true;
  yAxisLabel = 'Temperatur';

  moistures: Single[] = [];

  legend: boolean = true;
  timeline: boolean = true;
  xAxis: boolean = true;
  yAxis: boolean = true;
  temperatureCurve = shape.curveNatural;

  colorScheme = {
    domain: ['#27A75B', '#00ACC9', '#8DBF3F', '#E68878', '#FCEE66', '#88CAA3', '#C9D77B']
  };

  constructor(private dashboardService: DashboardService) {}

  ngOnInit(): void {
    this.getCurrentMoisture();
  }

  getCurrentMoisture(): void {
    this.dashboardService.getCurrentMoisture().subscribe(entries => this.moistures = entries) 
  }

  onSelect(event) {
    console.log(event);
  }

  temperatures = [
    {
      "name": "Left",
      "series": [
        {
          "name": "12",
          "value": 12
        },
        {
          "name": "13",
          "value": 14
        },
        {
          "name": "14",
          "value": 15
        },
        {
          "name": "15",
          "value": 11
        },
        {
          "name": "16",
          "value": 7
        },
        {
          "name": "17",
          "value": 18
        }
      ]
    },
  
    {
      "name": "Right",
      "series": [
        {
          "name": "12",
          "value": 11
        },
        {
          "name": "13",
          "value": 2
        },
        {
          "name": "14",
          "value": 16
        },
        {
          "name": "15",
          "value": 2
        },
        {
          "name": "16",
          "value": 21
        },
        {
          "name": "17",
          "value": 11
        }
      ]
    }
  ];
  

}
