import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { ContestformFormService } from './contestform-form.service';
import { ContestformService } from '../service/contestform.service';
import { IContestform } from '../contestform.model';
import { IContest } from 'app/entities/contest/contest.model';
import { ContestService } from 'app/entities/contest/service/contest.service';

import { ContestformUpdateComponent } from './contestform-update.component';

describe('Contestform Management Update Component', () => {
  let comp: ContestformUpdateComponent;
  let fixture: ComponentFixture<ContestformUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let contestformFormService: ContestformFormService;
  let contestformService: ContestformService;
  let contestService: ContestService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [ContestformUpdateComponent],
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
      .overrideTemplate(ContestformUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ContestformUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    contestformFormService = TestBed.inject(ContestformFormService);
    contestformService = TestBed.inject(ContestformService);
    contestService = TestBed.inject(ContestService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Contest query and add missing value', () => {
      const contestform: IContestform = { id: 456 };
      const contest: IContest = { id: 43099 };
      contestform.contest = contest;

      const contestCollection: IContest[] = [{ id: 79957 }];
      jest.spyOn(contestService, 'query').mockReturnValue(of(new HttpResponse({ body: contestCollection })));
      const additionalContests = [contest];
      const expectedCollection: IContest[] = [...additionalContests, ...contestCollection];
      jest.spyOn(contestService, 'addContestToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ contestform });
      comp.ngOnInit();

      expect(contestService.query).toHaveBeenCalled();
      expect(contestService.addContestToCollectionIfMissing).toHaveBeenCalledWith(
        contestCollection,
        ...additionalContests.map(expect.objectContaining)
      );
      expect(comp.contestsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const contestform: IContestform = { id: 456 };
      const contest: IContest = { id: 88679 };
      contestform.contest = contest;

      activatedRoute.data = of({ contestform });
      comp.ngOnInit();

      expect(comp.contestsSharedCollection).toContain(contest);
      expect(comp.contestform).toEqual(contestform);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IContestform>>();
      const contestform = { id: 123 };
      jest.spyOn(contestformFormService, 'getContestform').mockReturnValue(contestform);
      jest.spyOn(contestformService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ contestform });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: contestform }));
      saveSubject.complete();

      // THEN
      expect(contestformFormService.getContestform).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(contestformService.update).toHaveBeenCalledWith(expect.objectContaining(contestform));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IContestform>>();
      const contestform = { id: 123 };
      jest.spyOn(contestformFormService, 'getContestform').mockReturnValue({ id: null });
      jest.spyOn(contestformService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ contestform: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: contestform }));
      saveSubject.complete();

      // THEN
      expect(contestformFormService.getContestform).toHaveBeenCalled();
      expect(contestformService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IContestform>>();
      const contestform = { id: 123 };
      jest.spyOn(contestformService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ contestform });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(contestformService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareContest', () => {
      it('Should forward to contestService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(contestService, 'compareContest');
        comp.compareContest(entity, entity2);
        expect(contestService.compareContest).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
