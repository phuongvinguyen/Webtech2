import { Component } from '@angular/core';

@Component({
  selector: 'app-searchbar',
  templateUrl: './searchbar.component.html',
  styleUrls: ['./searchbar.component.sass']
})
export class SearchBarComponent {
  searchTerm: string = '';

  submitSearch() {
    this.search();
  }
  search() {
    // Hier können Sie die Logik für die Suche implementieren
    console.log('Suche nach:', this.searchTerm);
  }

  clear() {
    this.searchTerm = '';
  }
}