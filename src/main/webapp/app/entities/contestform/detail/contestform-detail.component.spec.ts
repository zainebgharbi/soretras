import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ContestformDetailComponent } from './contestform-detail.component';

describe('Contestform Management Detail Component', () => {
  let comp: ContestformDetailComponent;
  let fixture: ComponentFixture<ContestformDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ContestformDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ contestform: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(ContestformDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(ContestformDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load contestform on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.contestform).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
