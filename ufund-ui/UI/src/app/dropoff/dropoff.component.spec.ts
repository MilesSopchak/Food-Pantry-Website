import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DropoffComponent } from './dropoff.component';

describe('DropoffComponent', () => {
  let component: DropoffComponent;
  let fixture: ComponentFixture<DropoffComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [DropoffComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(DropoffComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
