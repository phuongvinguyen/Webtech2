import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-search-timeline',
  templateUrl: './search.component.html',
  styleUrls: ['./search.component.sass'],
})
export class SearchTimelineComponent {
  constructor(private route: ActivatedRoute) {}
}
