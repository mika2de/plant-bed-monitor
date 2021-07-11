import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MoistureComponent } from './moisture.component';

describe('MoistureComponent', () => {
  let component: MoistureComponent;
  let fixture: ComponentFixture<MoistureComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ MoistureComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(MoistureComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
