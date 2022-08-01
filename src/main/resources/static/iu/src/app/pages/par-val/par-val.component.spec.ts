import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ParValComponent } from './par-val.component';

describe('ParValComponent', () => {
  let component: ParValComponent;
  let fixture: ComponentFixture<ParValComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ParValComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ParValComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
