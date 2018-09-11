import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IWhitelist } from 'app/shared/model/whitelist.model';
import { WhitelistService } from './whitelist.service';

@Component({
    selector: 'jhi-whitelist-delete-dialog',
    templateUrl: './whitelist-delete-dialog.component.html'
})
export class WhitelistDeleteDialogComponent {
    whitelist: IWhitelist;

    constructor(private whitelistService: WhitelistService, public activeModal: NgbActiveModal, private eventManager: JhiEventManager) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.whitelistService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'whitelistListModification',
                content: 'Deleted an whitelist'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-whitelist-delete-popup',
    template: ''
})
export class WhitelistDeletePopupComponent implements OnInit, OnDestroy {
    private ngbModalRef: NgbModalRef;

    constructor(private activatedRoute: ActivatedRoute, private router: Router, private modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ whitelist }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(WhitelistDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
                this.ngbModalRef.componentInstance.whitelist = whitelist;
                this.ngbModalRef.result.then(
                    result => {
                        this.router.navigate([{ outlets: { popup: null } }], { replaceUrl: true, queryParamsHandling: 'merge' });
                        this.ngbModalRef = null;
                    },
                    reason => {
                        this.router.navigate([{ outlets: { popup: null } }], { replaceUrl: true, queryParamsHandling: 'merge' });
                        this.ngbModalRef = null;
                    }
                );
            }, 0);
        });
    }

    ngOnDestroy() {
        this.ngbModalRef = null;
    }
}
