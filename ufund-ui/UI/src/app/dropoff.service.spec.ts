import { TestBed } from '@angular/core/testing';

import { DropoffService } from './dropoff.service';

describe('DropoffService', () => {
  let service: DropoffService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(DropoffService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
