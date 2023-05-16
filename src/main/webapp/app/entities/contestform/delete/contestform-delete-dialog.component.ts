import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IContestform } from '../contestform.model';
import { ContestformService } from '../service/contestform.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './contestform-delete-dialog.component.html',
})
export class ContestformDeleteDialogComponent {
  contestform?: IContestform;

  constructor(protected contestformService: ContestformService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.contestformService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
