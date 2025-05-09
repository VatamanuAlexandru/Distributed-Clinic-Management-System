import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DoctorByServiceComponent } from './doctor-by-service.component';

describe('DoctorByServiceComponent', () => {
  let component: DoctorByServiceComponent;
  let fixture: ComponentFixture<DoctorByServiceComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [DoctorByServiceComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(DoctorByServiceComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
