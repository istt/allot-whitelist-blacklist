import { Component, OnInit } from '@angular/core';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';

import { Observable } from 'rxjs/Observable';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiDataUtils } from 'ng-jhipster';

import { DataFile } from './data-file.model';
import { DataFilePopupService } from './data-file-popup.service';
import { DataFileService } from './data-file.service';

@Component({
    selector: 'jhi-data-file-dialog',
    templateUrl: './data-file-dialog.component.html'
})
export class DataFileDialogComponent implements OnInit {
    dataFile: DataFile;
    isSaving: boolean;

    constructor(
        public activeModal: NgbActiveModal,
        private dataUtils: JhiDataUtils,
        private dataFileService: DataFileService,
        private eventManager: JhiEventManager
    ) {}

    ngOnInit() {
        this.isSaving = false;
    }

    byteSize(field) {
        return this.dataUtils.byteSize(field);
    }

    openFile(contentType, field) {
        return this.dataUtils.openFile(contentType, field);
    }

    setFileData(event, entity, field, isImage) {
        this.dataUtils.setFileData(event, entity, field, isImage);
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        this.subscribeToSaveResponse(this.dataFileService.create(this.dataFile));
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<DataFile>>) {
        result.subscribe((res: HttpResponse<DataFile>) => this.onSaveSuccess(res.body), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess(result: DataFile) {
        this.eventManager.broadcast({ name: 'blacklistListModification', content: 'OK' });
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }
}

@Component({
    selector: 'jhi-data-file-popup',
    template: ''
})
export class DataFilePopupComponent implements OnInit {
    constructor(private dataFilePopupService: DataFilePopupService) {}

    ngOnInit() {
        this.dataFilePopupService.open(DataFileDialogComponent as Component);
    }
}
