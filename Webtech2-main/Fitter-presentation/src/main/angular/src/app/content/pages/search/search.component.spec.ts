import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SearchTimelineComponent } from './search.component';

describe('SearchTimelineComponent', () => {
  let component: SearchTimelineComponent;
  let fixture: ComponentFixture<SearchTimelineComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [SearchTimelineComponent]
    });
    fixture = TestBed.createComponent(SearchTimelineComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
