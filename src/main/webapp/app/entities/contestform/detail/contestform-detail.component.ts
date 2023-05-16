import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IContestform } from '../contestform.model';

@Component({
  selector: 'jhi-contestform-detail',
  templateUrl: './contestform-detail.component.html',
})
export class ContestformDetailComponent implements OnInit {
  contestform: IContestform | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ contestform }) => {
      this.contestform = contestform;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
