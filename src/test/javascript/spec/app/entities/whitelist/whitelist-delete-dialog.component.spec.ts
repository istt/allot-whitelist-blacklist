/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { AppTestModule } from '../../../test.module';
import { WhitelistDeleteDialogComponent } from 'app/entities/whitelist/whitelist-delete-dialog.component';
import { WhitelistService } from 'app/entities/whitelist/whitelist.service';

describe('Component Tests', () => {
    describe('Whitelist Management Delete Component', () => {
        let comp: WhitelistDeleteDialogComponent;
        let fixture: ComponentFixture<WhitelistDeleteDialogComponent>;
        let service: WhitelistService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [AppTestModule],
                declarations: [WhitelistDeleteDialogComponent]
            })
                .overrideTemplate(WhitelistDeleteDialogComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(WhitelistDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(WhitelistService);
            mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
            mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
        });

        describe('confirmDelete', () => {
            it('Should call delete service on confirmDelete', inject(
                [],
                fakeAsync(() => {
                    // GIVEN
                    spyOn(service, 'delete').and.returnValue(of({}));

                    // WHEN
                    comp.confirmDelete(123);
                    tick();

                    // THEN
                    expect(service.delete).toHaveBeenCalledWith(123);
                    expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                    expect(mockEventManager.broadcastSpy).toHaveBeenCalled();
                })
            ));
        });
    });
});
