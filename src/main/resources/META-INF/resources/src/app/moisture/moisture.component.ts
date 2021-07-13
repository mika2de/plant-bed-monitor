import { Component, OnInit } from '@angular/core';

import { Multi } from "../multi"
import { MoistureService } from '../moisture.service';

@Component({
  selector: 'app-moisture',
  templateUrl: './moisture.component.html',
  styleUrls: ['./moisture.component.css']
})
export class MoistureComponent implements OnInit {

  moisturesRight: Multi[] = [];
  moisturesLeft: Multi[] = [];
  view: any[] = [700, 300];

  // options
  legend: boolean = true;
  legendPosition: string = 'right';
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


  constructor(private moistureService: MoistureService) {}

  ngOnInit(): void {
    this.getMoisturesLeft();
    this.getMoisturesRight();
  }

  getMoisturesLeft(): void {
    this.moistureService.getMoisturesLeft().subscribe(entries => this.moisturesLeft = entries) 
  }

  getMoisturesRight(): void {
    this.moistureService.getMoisturesRight().subscribe(entries => this.moisturesRight = entries) 
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
