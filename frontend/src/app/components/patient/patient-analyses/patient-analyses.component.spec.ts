import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PatientAnalysesComponent } from './patient-analyses.component';

describe('PatientAnalysesComponent', () => {
  let component: PatientAnalysesComponent;
  let fixture: ComponentFixture<PatientAnalysesComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PatientAnalysesComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(PatientAnalysesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
