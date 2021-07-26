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
  view: any[] = [700, 400];

  // options
  legend: boolean = true;
  legendPosition: string = 'below';
  showLabels: boolean = true;
  animations: boolean = true;
  xAxis: boolean = true;
  yAxis: boolean = true;
  showYAxisLabel: boolean = false;
  showXAxisLabel: boolean = false;
  xAxisLabel: string = 'Stunde';
  yAxisLabel: string = 'Bodenfeuchtigkeit';
  timeline: boolean = true;
  showRefLines: boolean = true;
  showRefLabels: boolean = true;
  referenceLines = [
    { value: 60, name: 'Max' },
    { value: 30, name: 'Min' },
    { value: 45, name: 'Ideal' }
  ];

  colorScheme = {
    domain: ['#5AA454', '#E44D25', '#CFC0BB', '#7aa3e5', '#a8385d', '#aae3f5']
  };


  constructor(private moistureService: MoistureService) {}

  ngOnInit(): void {
    this.getMoistures();
  }

  getMoistures(): void {
    this.moistureService.getMoistures().subscribe(entries => this.moisturesLeft = entries) 
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
