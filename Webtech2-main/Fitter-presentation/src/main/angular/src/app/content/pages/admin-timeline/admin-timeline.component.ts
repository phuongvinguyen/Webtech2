import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-admin-timeline',
  templateUrl: './admin-timeline.component.html',
  styleUrls: ['./admin-timeline.component.sass'],
})
export class AdminTimelineComponent {
  constructor(private route: ActivatedRoute) {}
}
