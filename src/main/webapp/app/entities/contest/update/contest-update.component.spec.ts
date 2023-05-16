import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { ContestFormService } from './contest-form.service';
import { ContestService } from '../service/contest.service';
import { IContest } from '../contest.model';

import { ContestUpdateComponent } from './contest-update.component';

describe('Contest Management Update Component', () => {
  let comp: ContestUpdateComponent;
  let fixture: ComponentFixture<ContestUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let contestFormService: ContestFormService;
  let contestService: ContestService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [ContestUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(ContestUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ContestUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    contestFormService = TestBed.inject(ContestFormService);
    contestService = TestBed.inject(ContestService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const contest: IContest = { id: 456 };

      activatedRoute.data = of({ contest });
      comp.ngOnInit();

      expect(comp.contest).toEqual(contest);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IContest>>();
      const contest = { id: 123 };
      jest.spyOn(contestFormService, 'getContest').mockReturnValue(contest);
      jest.spyOn(contestService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ contest });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: contest }));
      saveSubject.complete();

      // THEN
      expect(contestFormService.getContest).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(contestService.update).toHaveBeenCalledWith(expect.objectContaining(contest));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IContest>>();
      const contest = { id: 123 };
      jest.spyOn(contestFormService, 'getContest').mockReturnValue({ id: null });
      jest.spyOn(contestService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ contest: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: contest }));
      saveSubject.complete();

      // THEN
      expect(contestFormService.getContest).toHaveBeenCalled();
      expect(contestService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IContest>>();
      const contest = { id: 123 };
      jest.spyOn(contestService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ contest });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(contestService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
