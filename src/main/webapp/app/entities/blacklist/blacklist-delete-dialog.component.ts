import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IBlacklist } from 'app/shared/model/blacklist.model';
import { BlacklistService } from './blacklist.service';

@Component({
    selector: 'jhi-blacklist-delete-dialog',
    templateUrl: './blacklist-delete-dialog.component.html'
})
export class BlacklistDeleteDialogComponent {
    blacklist: IBlacklist;

    constructor(private blacklistService: BlacklistService, public activeModal: NgbActiveModal, private eventManager: JhiEventManager) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.blacklistService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'blacklistListModification',
                content: 'Deleted an blacklist'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-blacklist-delete-popup',
    template: ''
})
export class BlacklistDeletePopupComponent implements OnInit, OnDestroy {
    private ngbModalRef: NgbModalRef;

    constructor(private activatedRoute: ActivatedRoute, private router: Router, private modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ blacklist }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(BlacklistDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
                this.ngbModalRef.componentInstance.blacklist = blacklist;
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
