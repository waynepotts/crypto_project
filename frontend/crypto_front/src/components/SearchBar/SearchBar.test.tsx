import { render, screen, fireEvent } from '@testing-library/react';
import { SearchBar } from './SearchBar';

describe('SearchBar', () => {
  test('renders search input', () => {
    render(<SearchBar searchQuery="" setSearchQuery={() => {}} isLoading={false} />);
    const input = screen.getByPlaceholderText("Search currencies...");
    expect(input).toBeInTheDocument();
  });

  test('displays current search query value', () => {
    render(<SearchBar searchQuery="bitcoin" setSearchQuery={() => {}} isLoading={false} />);
    const input = screen.getByPlaceholderText("Search currencies...");
    expect(input).toHaveValue("bitcoin");
  });

  test('calls setSearchQuery on input change', () => {
    const setSearchQuery = vi.fn();
    render(<SearchBar searchQuery="" setSearchQuery={setSearchQuery} isLoading={false} />);
    const input = screen.getByPlaceholderText("Search currencies...");
    fireEvent.change(input, { target: { value: "eth" } });
    expect(setSearchQuery).toHaveBeenCalledWith("eth");
  });

  test('disables input when loading', () => {
    render(<SearchBar searchQuery="" setSearchQuery={() => {}} isLoading={true} />);
    const input = screen.getByPlaceholderText("Search currencies...");
    expect(input).toBeDisabled();
  });

  test('renders search icon', () => {
    const { container } = render(<SearchBar searchQuery="" setSearchQuery={() => {}} isLoading={false} />);
    const searchIcon = container.querySelector('svg');
    expect(searchIcon).toBeInTheDocument();
  });
});
