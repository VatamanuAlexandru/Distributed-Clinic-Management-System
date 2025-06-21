import { TestBed } from '@angular/core/testing';

import { TestParameterResultService } from './test-parameter-result.service';

describe('TestParameterResultService', () => {
  let service: TestParameterResultService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(TestParameterResultService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
