import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AdminPostComponent } from './admin-post.component';

describe('AdminPostComponent', () => {
  let component: AdminPostComponent;
  let fixture: ComponentFixture<AdminPostComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [AdminPostComponent]
    });
    fixture = TestBed.createComponent(AdminPostComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
