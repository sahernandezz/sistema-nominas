import {Component, OnInit, Output, ViewChild} from '@angular/core';
import {NgbModal} from "@ng-bootstrap/ng-bootstrap";
import {EventEmitter} from "protractor";

@Component({
  selector: 'app-load-modal',
  templateUrl: './load-modal.component.html',
  styleUrls: ['./load-modal.component.scss']
})
export class LoadModalComponent implements OnInit {
  @ViewChild('contentModal') modal: any;
  @Output('on') on = new EventEmitter();
  @Output('off') off = new EventEmitter();

  constructor(public modalService: NgbModal) {

  }

  ngOnInit(): void {

  }

  public modalOpen(): void {
    this.on.emit('modalOpen');
    this.open(this.modal);
  }

  public modalClose(): void {
    this.off.emit('modalClose');
    this.modalService.dismissAll('Cross click');
  }

  private open(content: any) {
    this.modalService.open(content, {
      centered: true,
      ariaLabelledBy: 'modal-basic-title',
      backdrop: 'static',
    });
  }

}
