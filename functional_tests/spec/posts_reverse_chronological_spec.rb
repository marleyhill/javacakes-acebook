feature 'peeps are displayed in reverse chronological order' do
  scenario 'user can see latest posts first' do
    visit('/')
    fill_in :name, with: 'user'
    fill_in :email, with: "email@me.com"
    fill_in :password, with: "password123"
    find('input[type=submit]').first.click
    visit('/posts')
    fill_in :post, with: 'This is the second test'
    click_button 'Submit'
    expect(page.li.first).to have_content /This is the second test/
  end
end