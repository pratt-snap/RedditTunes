import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RedditorComponent } from './redditor.component';

describe('RedditorComponent', () => {
  let component: RedditorComponent;
  let fixture: ComponentFixture<RedditorComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ RedditorComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(RedditorComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
