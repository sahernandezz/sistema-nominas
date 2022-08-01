import { ComponentFixture, TestBed } from '@angular/core/testing';

import { FormatoArchivoComponent } from './formato-archivo.component';

describe('FormatoArchivoComponent', () => {
  let component: FormatoArchivoComponent;
  let fixture: ComponentFixture<FormatoArchivoComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ FormatoArchivoComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(FormatoArchivoComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
