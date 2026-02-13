import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AdminExports } from './admin-exports';

describe('AdminExports', () => {
  let component: AdminExports;
  let fixture: ComponentFixture<AdminExports>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AdminExports]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AdminExports);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
