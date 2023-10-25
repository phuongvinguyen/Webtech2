import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TimlimePostComponent } from './timlime-post.component';

describe('TimlimePostComponent', () => {
  let component: TimlimePostComponent;
  let fixture: ComponentFixture<TimlimePostComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [TimlimePostComponent]
    });
    fixture = TestBed.createComponent(TimlimePostComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
