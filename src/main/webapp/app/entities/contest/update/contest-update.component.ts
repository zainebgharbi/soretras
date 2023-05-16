import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { ContestFormService, ContestFormGroup } from './contest-form.service';
import { IContest } from '../contest.model';
import { ContestService } from '../service/contest.service';
import { elevel } from 'app/entities/enumerations/elevel.model';

@Component({
  selector: 'jhi-contest-update',
  templateUrl: './contest-update.component.html',
})
export class ContestUpdateComponent implements OnInit {
  isSaving = false;
  contest: IContest | null = null;
  elevelValues = Object.keys(elevel);

  editForm: ContestFormGroup = this.contestFormService.createContestFormGroup();

  constructor(
    protected contestService: ContestService,
    protected contestFormService: ContestFormService,
    protected activatedRoute: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ contest }) => {
      this.contest = contest;
      if (contest) {
        this.updateForm(contest);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const contest = this.contestFormService.getContest(this.editForm);
    if (contest.id !== null) {
      this.subscribeToSaveResponse(this.contestService.update(contest));
    } else {
      this.subscribeToSaveResponse(this.contestService.create(contest));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IContest>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(contest: IContest): void {
    this.contest = contest;
    this.contestFormService.resetForm(this.editForm, contest);
  }
}
