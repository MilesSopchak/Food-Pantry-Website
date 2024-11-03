import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AdminscheduleComponent } from './adminschedule.component';

describe('AdminscheduleComponent', () => {
  let component: AdminscheduleComponent;
  let fixture: ComponentFixture<AdminscheduleComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [AdminscheduleComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(AdminscheduleComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
