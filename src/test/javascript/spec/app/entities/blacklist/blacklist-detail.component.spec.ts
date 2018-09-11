/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { AppTestModule } from '../../../test.module';
import { BlacklistDetailComponent } from 'app/entities/blacklist/blacklist-detail.component';
import { Blacklist } from 'app/shared/model/blacklist.model';

describe('Component Tests', () => {
    describe('Blacklist Management Detail Component', () => {
        let comp: BlacklistDetailComponent;
        let fixture: ComponentFixture<BlacklistDetailComponent>;
        const route = ({ data: of({ blacklist: new Blacklist(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [AppTestModule],
                declarations: [BlacklistDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(BlacklistDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(BlacklistDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.blacklist).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
