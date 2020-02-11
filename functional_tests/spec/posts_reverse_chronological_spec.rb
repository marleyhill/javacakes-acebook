feature 'posts are displayed in reverse chronological order' do
  scenario 'user can see latest posts first' do
    visit('/')
    fill_in :name, with: 'user'
    fill_in :email, with: "email@me.com"
    fill_in :password, with: "password123"
    click_button('Sign up')

    visit('/posts')
    fill_in :post, with: 'This is the first test post'
    click_button 'Submit'
    fill_in :post, with: 'This is the second test post'
    click_button 'Submit'

    expect("This is the second test post").to appear_before("This is the first test post")
  end
end