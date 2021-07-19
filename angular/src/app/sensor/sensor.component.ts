import { Component, OnInit } from '@angular/core';
import { Sensor } from '../sensor'
import { SensorService } from '../sensor.service';

@Component({
  selector: 'app-sensor',
  templateUrl: './sensor.component.html',
  styleUrls: ['./sensor.component.css']
})
export class SensorComponent implements OnInit {

  databaseSensors: Sensor[] = []
  modelSensors: Sensor[] = []

  constructor(private sensorService: SensorService) {}

  ngOnInit(): void {
    this.getSensors();
  }

  getSensors(): void {
    this.sensorService.getSensors().subscribe(entries => {
      this.databaseSensors = entries
      this.modelSensors = entries.map(a => ({...a}))
    }) 
  }
  submitted = false;

  onSubmit() { this.submitted = true; 
    const changedSensors = this.modelSensors.filter(({ name: id1 }) => !this.databaseSensors.some(({ name: id2 }) => id2 === id1));
    const sensors = this.sensorService.updateSensors(changedSensors)
    console.log(sensors)
  }
}
