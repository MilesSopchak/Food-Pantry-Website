import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AdminNeedsDetailComponent } from './admin-needs-detail.component';

describe('AdminNeedsDetailComponent', () => {
  let component: AdminNeedsDetailComponent;
  let fixture: ComponentFixture<AdminNeedsDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [AdminNeedsDetailComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(AdminNeedsDetailComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
