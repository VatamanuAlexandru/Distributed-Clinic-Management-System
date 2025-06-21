import { TestBed } from '@angular/core/testing';

import { MedicalTestService } from './medical-test.service';

describe('MedicalTestService', () => {
  let service: MedicalTestService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(MedicalTestService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
